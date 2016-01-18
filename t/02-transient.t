use v6;

use lib './lib';
use Test;
use Data::Serialize::Serializable;
use Data::Serialize::Transient;

class A does Serializable {
  has $.a;
  has $.b is transient;
}

ok !isTransient(A.^attributes[0]), '$.a is a non-transient field';
ok isTransient(A.^attributes[1]), '$.b is a transient field';

done-testing;
