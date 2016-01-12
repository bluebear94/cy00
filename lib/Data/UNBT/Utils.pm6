use v6;

use experimental :pack;

unit module Data::NBT::Utils;

sub readByte($handle --> uint8) is export {
  return $handle.read(1)[0];
}

sub writeByte($handle, uint8 $d) is export {
  $handle.write(Blob.new($d))
}

sub readShort($handle --> uint16) is export {
  return $handle.read(2).unpack("n");
}

sub writeShort($handle, uint16 $d) is export {
  $handle.write(pack("n", $d));
}

sub readInt($handle --> uint32) is export {
  return $handle.read(4).unpack("N");
}

sub writeInt($handle, uint32 $d) is export {
  $handle.write(pack("N", $d));
}

sub readLong($handle --> uint64) is export {
  my ($upper, $lower) = $handle.read(8).unpack("N N");
  return ($upper +< 32) +| $lower;
}

sub writeLong($handle, uint64 $d) is export {
  $handle.write(pack("N N", $d +> 32, $d +& 32));
}
