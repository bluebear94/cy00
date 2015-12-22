use v6;

use HTTP::Easy;
use HTTP::Status;

unit class Interface::Basic does HTTP::Easy;

has Buf $!cookie;
has Str $.password;
has Bool $!connected;
has &!callback;
has @!dependencies;

my %contentTypes =
  "html" => "text/html",
  "js" => "text/js",
  "css" => "text/css",
  "woff" => "binary/woff",
  "ttf" => "binary/ttf",
  "eot" => "binary/eot"
  ;

sub generate($status, @headers, @con) {
  my $content = [~] @con.map({$^s ~~ Blob ?? $^s !! "$^s\r\n".encode});
  @headers.push: ("Content-Length" => $content.bytes);
  my $top = "HTTP/1.1 $status {get_http_status_msg($status)}\r\n".encode;
  my $dt = DateTime.now();
  my $gh = "Date: $dt\r\nConnection: close\r\n".encode;
  my $rh = "Server: WebRPG/cy00\r\nAccept-Ranges: bytes\r\n".encode;
  my $eh = @headers.map({"{$_.key}: {$_.value}"}).join("\r\n").encode;
  my $res = $top ~ $gh ~ $rh ~ $eh ~ "\r\n\r\n".encode ~ $content;
  return $res;
}

sub rawMessage($sc, $body) {
  return generate($sc, ['Content-Type' => 'text/plain']. [$body]);
}

method handler {
  my $path = %!env<PATH_INFO>;
  $path = "/index.html" if $path eq "/";
  if $path ~~ / '/game/' (.*) / {
    my $req = $0;
    given $req {
      when "login" {
        try {
          return rawMessage(400, "Already connected!") if $!connected;
          my $password = $.body.decode;
          my $success = !($!password.defined) || ($!password eq $password);
          if $success {
            $!connected = True;
            return generate(200, ['Content-Type' => 'binary/raw'], [$!cookie]);
          }
          return rawMessage(401, "Wrong password!");
          CATCH {
            default {return rawMessage(400, "Password is not a valid UTF-8 string!"]) if $!connected;}
          }
        }
      }
      when * {
        # TODO implement game stuff
        return generate(404, ['Content-Type' => 'text/html'], ["<h1>404 not found!</h1>"]);
      }
    }
  } else {
    my $fullpath = "assets/web$path";
    return generate(404, ['Content-Type' => 'text/html'], ["<h1>404 not found!</h1>"]) unless $fullpath.IO.f;
    my $extension = "";
    $extension = $0 if $path ~~ / '.' (.*) /;
    my $ct = %contentTypes{$extension} // 'text/plain';
    return generate(200, ['Content-Type' => $ct], [slurp($fullpath, :bin($ct.starts-with("bin")))]);
  }
}

submethod BUILD(:&!callback, :@!dependencies, :$password, :$port, :$debug) {
  $!cookie = "/dev/urandom".IO.open.read(128);
  $!connected = False;
  $!port = $port;
  $!debug = $debug;
  $!password = $password;
}
