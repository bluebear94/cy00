use v6;

use lib './lib';
use Interface::Basic;

#= Starts a cy00 server.
sub MAIN(Int :$port = 5835, Str :$password = (Str), :$debug = False) {
  my $server = Interface::Basic.new(:$port, :$password, :$debug);
  say "Please go to localhost:$port on your browser.";
  $server.run;
  END {say "bye bye";}
}

=begin pod
Starts a cy00 server.

You can set a password, and should if you plan on port forwarding and
connecting remotely; otherwise someone might steal your connection.
=end pod
