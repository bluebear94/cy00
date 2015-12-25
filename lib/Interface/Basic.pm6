use v6;

use HTTP::Easy;
use HTTP::Status;
use MIME::Base64;

unit class Interface::Basic does HTTP::Easy;

has Blob $!cookie;
has Str $!cookie64;
has Str $.password;
has Numeric $.tps = 10;
has Bool $!connected;
has &!callback;
has @!dependencies;
has Channel $!keypresses = Channel.new;
has Channel $!issues = Channel.new;
has $!tick = 0;
has $!lastActedTick = 0;

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
  $content = Buf.new unless $content; # make sure $content isn't a Str
  @headers.push: ("Content-Length" => $content.bytes);
  my $top = "HTTP/1.1 $status {get_http_status_msg($status)}\r\n".encode;
  my $dt = DateTime.now();
  say $dt;
  my $gh = "Date: $dt\r\nConnection: close\r\n".encode;
  my $rh = "Server: WebRPG/cy00\r\nAccept-Ranges: bytes\r\n".encode;
  my $eh = @headers.map({"{$_.key}: {$_.value}"}).join("\r\n").encode;
  my $res = $top ~ $gh ~ $rh ~ $eh ~ "\r\n\r\n".encode ~ $content;
  #say $res if $res ~~ Blob && $res.bytes < 1000;
  return $res;
}

sub rawMessage($sc, $body) {
  return generate($sc, ['Content-Type' => 'text/plain'], [$body]);
}

method handler {
  my $path = %!env<PATH_INFO>;
  $path = "/index.html" if $path eq "/";
  if $path ~~ / '/game/' (.*) / {
    my $req = $0;
    given $req {
      when "login" {
        return self.login;
      }
      when "inquire" {
        return self.answerInquiry;
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

method login {
  try {
    return rawMessage(400, "Already connected!") if $!connected;
    my $password = $.body.decode;
    my $success = !($!password.defined) || ($!password eq $password);
    if $success {
      $!connected = True;
      return rawMessage(200, $!cookie64);
    }
    return rawMessage(401, "Wrong password or none was provided!");
    CATCH {
      default {return rawMessage(400, "Password is not a valid UTF-8 string!") if $!connected;}
    }
  }
}

method answerInquiry {
  say $.body.decode;
  my ($cookie, $presses) = $.body.decode.split(/ \r\n|\n|\r /);
  return rawMessage(401, "Invalid cookie! The session may have expired.") if $cookie ne $!cookie64;
  my @presses = $presses.split(" ");
  self.registerKeypresses(@presses);
  # how much delay?
  sleep(0.25 / $!tps);
  #$oldIssues.close;
  #my @issues = $oldIssues.list;
  my @issues = gather while (my $issue = $!issues.poll).defined {take $issue;};
  return self.returnIssues(@issues);
}

method registerKeypresses(@presses) {
  $!keypresses.send({ codes => @presses.Set, time => $!tick });
  self.update if @presses;
}

method returnIssues(@issues) {
  my @lines = gather for @issues -> $p {
    take $p.key;
    take $p.value.subst(/ \r\n|\n|\r /, "\\n", :g);
  }
  #die "自殺";
  return generate(200, ['Content-Type' => 'text/plain'], @lines);
}

method update {
  $!lastActedTick = $!tick;
}

submethod BUILD(:&!callback, :@!dependencies, :$password, :$port, :$debug) {
  self.resetCookie;
  $!connected = False;
  $!port = $port;
  $!debug = $debug;
  $!password = $password;
}

submethod resetCookie {
  try {
    $!cookie = "/dev/urandom".IO.open.read(128);
    CATCH {
      default {
        $!cookie = Buf.new(|(256.rand.Int xx 128));
      }
    }
  }
  $!cookie64 = MIME::Base64.encode($!cookie).subst(/ \r\n|\n|\r /, "", :g);
}
