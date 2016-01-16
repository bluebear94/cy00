use v6;

unit module Data::Serialize::Transient;

role TransientAttribute {}

multi sub trait_mod:<is> (Attribute:D $attr, :$transient!) is export {
  $attr does TransientAttribute;
}

sub isTransient(Attribute $attr) is export {
  $attr ~~ TransientAttribute;
}
