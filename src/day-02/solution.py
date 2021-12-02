from typing import List, Tuple


def parse_line(line: str) -> Tuple[str, int]:
    name, x = line.strip().split(" ")
    return (name, int(x))


with open("input.txt") as f:
    input = list(map(parse_line, f.readlines()))


def part1(commands: List[Tuple[str, int]]) -> int:
    pos = [0, 0]

    for name, amount in commands:
        if name == "forward":
            pos[0] += amount
        if name == "down":
            pos[1] += amount
        if name == "up":
            pos[1] -= amount

    return pos[0] * pos[1]


def part2(commands: List[Tuple[str, int]]) -> int:
    pos = [0, 0, 0]

    for name, amount in commands:
        if name == "forward":
            pos[0] += amount
            pos[1] += amount * pos[2]
        if name == "down":
            pos[2] += amount
        if name == "up":
            pos[2] -= amount

    return pos[0] * pos[1]


print(part1(input))
print(part2(input))
