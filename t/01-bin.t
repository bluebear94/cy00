use v6;

use lib './lib';

use Test;
use Statistics::Bin;

class TestBin does Statistics::Bin[Int] {
  method capacity {
    return 100;
  }
}

{
  my $bin = TestBin.new(current => 70);
  ok ($bin + 20).current == 90, 'add to bin';
  ok ($bin - 20).current == 50, 'subtract from bin';
  ok ($bin + 40).current == 100, 'add to bin with overflow';
  ok ($bin - 120).current == 0, 'subtract from bin with underflow';
}

done-testing;
