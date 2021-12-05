use itertools::Itertools;
use std::collections::HashMap;

type Vent = (i32, i32, i32, i32);

fn main() {
    let vents = include_str!("in")
        .lines()
        .map(parse)
        .collect::<Vec<_>>();

    dbg!(part1(&vents));
    dbg!(part2(&vents));
}

fn parse(line: &str) -> Vent {
    line.split(" -> ")
        .flat_map(|s| s.split(","))
        .map(|i| i.parse().unwrap())
        .collect_tuple()
        .unwrap()
}

fn part1(vents: &[Vent]) -> usize {
    let no_daigonals = vents
        .iter()
        .copied()
        .filter(|(x1, y1, x2, y2)| x1 == x2 || y1 == y2);
    count_intersections(no_daigonals)
}

fn part2(vents: &[Vent]) -> usize {
    count_intersections(vents.iter().copied())
}

fn count_intersections<Vents>(vents: Vents) -> usize
where
    Vents: Iterator<Item = Vent>,
{
    let mut points = HashMap::new();
    for (x1, y1, x2, y2) in vents {
        let dx = (x2 - x1).signum();
        let dy = (y2 - y1).signum();
        let mut x = x1;
        let mut y = y1;
        while (x, y) != (x2 + dx, y2 + dy) {
            *points.entry((x, y)).or_insert(0) += 1;
            x += dx;
            y += dy;
        }
    }
    points.values().filter(|&&n| n > 1).count()
}
