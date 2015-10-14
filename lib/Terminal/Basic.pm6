unit module Terminal::Basic;

sub dimensions is export {
  # http://rosettacode.org/wiki/Terminal_control/Dimensions#Perl_6
  my $stty = qx[stty -a];
  my $lines = $stty.match(/ 'rows '    <( \d+/);
  my $cols  = $stty.match(/ 'columns ' <( \d+/);
  if $lines == 0 || $cols == 0 {
    note "\e[31mCannot get window dimensions!";
    note "This might be caused by using the Windows Command Prompt."
    note "Please ensure that your window is as large as needed,";
    note "and please consider installing Cygwin.";
    note "\e[0m";
    return 1000, 1000;
  }
  return +$lines, +$cols;
}

sub setWindowTitle(Str $title) is export {
  print "\e]0;$title\a";
}

sub clear(Int $mode = 2) is export {
  print "\e[{$mode}J";
}

sub moveTo(Int $row, Int $col) is export {
  print "\e[$row;{$col}H";
}

sub moveTo0(Int $row, Int $col) is export {
  moveTo($row - 1, $col - 1);
}

sub sgr($code) is export {
  print "\e[{$code}m";
}

sub output(Int $row, Int $col, Str $msg) is export {
  moveTo($row, $col);
  print $msg;
}

sub output0(Int $row, Int $col, Str $msg) is export {
  moveTo0($row, $col);
  print $msg;
}

sub setTextColor(Int $col) is export {
  sgr(30 + $col);
}

sub setXTextColor(Int $red, Int $green, Int $blue) is export {
  sgr("38;2;$red;$green;$blue");
}

sub setBGColor(Int $col) is export {
  sgr(40 + $col);
}

sub setXBGColor(Int $red, Int $green, Int $blue) is export {
  sgr("48;2;$red;$green;$blue");
}
