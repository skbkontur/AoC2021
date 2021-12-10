# Solution to Advent Of Code 2021 - Day 10, Part I only.
#
# To run this you need to install Logica language:
# python3 -m pip install logica
# Then you can run this as:
# python3 -m logica day10.l run TotalCorruptionScore
#
# Written by Evgeny Skvortsov,
# you can reach me at https://twitter.com/evgskvdev


@Engine("sqlite");

# Uncomment for quick debugging:
# Input() = "{([(<{}[<>[]}>{[]{[(<()>";

# Uncomment to read the test input:
# Input() = ReadFile("test_input.txt");

# Production input:
Input() = ReadFile("prod_input.txt");

# Reading the lines into database:
@Ground(Line);
Line(i) = line :-
  lines == Split(Input(), SqlExpr("char(10)", {})),
  i in Range(Size(lines)),
  line == Element(lines, i);

# Mathing opening parenthesis to the closed parenthesis:
Couple("{") = "}";
Couple("[") = "]";
Couple("<") = ">";
Couple("(") = ")";

# Using the mapping above to identify parenthesis that open and ones
# that close. 
Opens(x) :- Couple(x);
Closes(Couple());

# Simplest non-empty complete strings look like (), [], etc. 
Core() = a ++ Couple(a);

# Convenience function extracting one character at a position.
At(l, i) = Substr(l, i, 1);

# Alright, let's solve the problem!

# Identifying smallest non-empty complete strings in the input:
CorePart(l, i, i + 1) :-
  l == Line(),
  i in Range(Length(l)),
  Substr(l, i, 2) == Core();

# Identifying complete substrings in the input strings:
@Recursive(CompletePart, 10);
@Ground(CompletePart);
# Core substring is complete.
CompletePart(l, i, j) :- CorePart(l, i, j);
# Two complete substrings side-by-side form a complete string.
CompletePart(l, i, k) :-
  CompletePart(l, i, j),
  CompletePart(l, j + 1, k);
# A complete string enclosed in parenthesis is complete.
CompletePart(l, i - 1, j + 1) :-
  CompletePart(l, i, j),
  Couple(At(l, i - 1)) == At(l, j + 1);

# A predicate to show complete strings for debugging:
@OrderBy(ShowComplete, "col1");
ShowComplete(l, i, j, p) :-
  CompletePart(l, i, j),
  p == Substr(l, i, j - i + 1);

# Identifying corruption.
# We have a corruption if we have a complete string, which is surrounded
# by an open and a close parenthesis, while the closing parenthesis does not
# match the open parenthesis.
BadSign(l, j + 1) = At(l, j + 1) :-
  CompletePart(l, i, j),
  a == At(l, i - 1),
  b == At(l, j + 1),
  Opens(a),
  Closes(b),
  Couple(a) != b;

# If a string starts with a complete string, followed by a closed parenthesis
# then we have corruption.
BadSign(l, j + 1) = At(l, j + 1) :-
  CompletePart(l, 1, j),
  b == At(l, j + 1),
  Closes(b);

# Corruption score function.
CorruptionScore(")") = 3;
CorruptionScore("]") = 57;
CorruptionScore("}") = 1197;
CorruptionScore(">") = 25137;

# Calculating total corruption score.
TotalCorruptionScore() += CorruptionScore(BadSign());

