use v6;

# We wanted to use the Japanese 瓶, but quite a few people have trouble
# typing (or even reading!) that so we'll settle with romaji.
unit role Statistics::Bin[::T = Int];

has T $.current;

method capacity() returns T { ... }

method add(T $x) {
  return self.clone(
    current => max(0, min(self.capacity, $!current + $x))
  );
}

multi infix:<+>(Statistics::Bin $b, $x) is export {
  return $b.add($x);
}

multi infix:<->(Statistics::Bin $b, $x) is export {
  return $b.add(-$x);
}
