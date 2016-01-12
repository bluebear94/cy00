use v6;

use Compress::Zlib;

use lib './lib';
use Data::UNBT::Utils;

unit module Data::UNBT;

enum TagName ("TagEnd", "TagByte", "TagShort", "TagInt", "TagLong", "TagFloat", "TagDouble",
  "TagByteArray", "TagString", "TagList", "TagCompound", "TagIntArray",
  "TagBigInt", "TagRational", "TagFunction",
  TagUByte => 129, "TagUShort", "TagUInt", "TagULong", "TagUFloat", "TagUDouble",
  TagBigString => 136, TagVarList => 137);

class UNBTNode is export {
  has $.type;
  has $.value;
  method Str {
    return $!value.Str;
  }
  method of($type, $val) {
    my $value = $val;
    given $type {
      $value = $val > 0 ?? $val !! 256 - $val when uint8;
      $value = $val > 0 ?? $val !! 65536 - $val when uint16;
      $value = $val > 0 ?? $val !! (1 +< 32) - $val when uint32;
      $value = $val > 0 ?? $val !! (1 +< 64) - $val when uint64;
    }
    return UNBTNode.new(type => $type, value => $value);
  }
}

sub readPayload($handle, TagName $tag) {
  given $tag {
    return Nil when TagEnd;
    return UNBTNode.of(int8, readByte($handle)) when TagByte;
    return UNBTNode.of(int16, readShort($handle)) when TagShort;
    return UNBTNode.of(int32, readInt($handle)) when TagInt;
    return UNBTNode.of(int64, readLong($handle)) when TagLong;
  }
}

sub readUncompressed($handle) is export {
  my TagName $tag = TagName(readByte($handle));
  return Nil if $tag == TagEnd;
}

=begin pod

Refer L<here|http://minecraft.gamepedia.com/NBT> for the original NBT.

=head1 Extensions

=head2 Extension tags

=item TAG_BigInt (12)
=item TAG_Rational (13)
=item TAG_Function (14)
=item TAG_BigString (136)
=item TAG_VarList (137)
=item Unsigned types are denoted by adding 128 to the ID.

=head2 Metadata

Metadata is optional, but the C<Meta> tag must not be empty. Any individual
fields are optional.

=item1 *
=item2 TAG_Compound Meta
=item3 TAG_Int version (20497 for UNBT)
=item3 TAG_Compound application
=item4 TAG_String name
=item4 TAG_String author
=item4 TAG_String version
=item4 TAG_String versionAlias
=item4 TAG_String language

=end pod
