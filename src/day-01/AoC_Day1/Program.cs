using System;
using System.IO;
using System.Linq;

// Put your input to "input.txt" file
var depths = File.ReadAllLines("input.txt").Select(int.Parse).ToList();

var answer1 = depths.Skip(1).Where((depth, i) => depth > depths[i]).Count();

var answer2 = depths.Skip(3).Where((depth, i) => 
    depth > depths[i]
).Count();

Console.WriteLine($"First puzzle answer: {answer1}, second puzzle answer: {answer2}");
