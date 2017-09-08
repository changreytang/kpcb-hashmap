# KPCB HashMap Implementation

#### How to Build
```text
mvn clean package
```

#### How to Run
```text
./run-hash-map.sh
```

#### Valid Commands
- set \<key\> \<value\>
- get \<key\>
- delete \<key\>
- load
- print

#### Implementation Details
- Collision resolution using double hashing, doesn't ensure insertion above load factor of 0.7
- Static size
- Use tombstones for deletion

