package main

import (
	"bufio"
	"os"
	"sort"
)

func main() {
	layout := readCavesLayout()
	println(calculateRisk(layout))

	lowestPoints := findLowestPoints(layout)
	basinsSizes := bfsBasinsSize(layout, lowestPoints)
	sort.Sort(sort.Reverse(sort.IntSlice(basinsSizes)))
	println(basinsSizes[0] * basinsSizes[1] * basinsSizes[2])

}

func readCavesLayout() [][]int {
	file, _ := os.Open("day-09/day09.txt")
	var layout [][]int
	scanner := bufio.NewScanner(file)

	for scanner.Scan() {
		line := scanner.Text()
		var numbers []int
		for _, ch := range line {
			numbers = append(numbers, int(ch-'0'))
		}
		layout = append(layout, numbers)
	}
	return layout
}

func calculateRisk(layout [][]int) int {
	risk := 0
	for i := 0; i < len(layout); i++ {
		for j := 0; j < len(layout[0]); j++ {
			// current element: (i, j)
			// neighbours: (i+1, j), (i-1, j), (i, j+1), (i, j-1)
			current := layout[i][j]
			var neighbours []int
			if i != 0 {
				neighbours = append(neighbours, layout[i-1][j])
			}
			if j != 0 {
				neighbours = append(neighbours, layout[i][j-1])
			}
			if i != len(layout)-1 {
				neighbours = append(neighbours, layout[i+1][j])
			}
			if j != len(layout[0])-1 {
				neighbours = append(neighbours, layout[i][j+1])
			}
			currentRisk := current + 1
			for _, neighbour := range neighbours {
				if current >= neighbour {
					currentRisk = 0
					break
				}
			}
			risk += currentRisk
		}
	}
	return risk
}

func findLowestPoints(layout [][]int) [][]int {
	var lowestPoints [][]int
	for i := 0; i < len(layout); i++ {
		for j := 0; j < len(layout[0]); j++ {
			// current element: (i, j)
			// neighbours: (i+1, j), (i-1, j), (i, j+1), (i, j-1)
			current := layout[i][j]
			var neighbours []int
			if i != 0 {
				neighbours = append(neighbours, layout[i-1][j])
			}
			if j != 0 {
				neighbours = append(neighbours, layout[i][j-1])
			}
			if i != len(layout)-1 {
				neighbours = append(neighbours, layout[i+1][j])
			}
			if j != len(layout[0])-1 {
				neighbours = append(neighbours, layout[i][j+1])
			}
			isLowest := true
			for _, neighbour := range neighbours {
				if current >= neighbour {
					isLowest = false
					break
				}
			}
			if isLowest {
				point := []int{i, j}
				lowestPoints = append(lowestPoints, point)
			}
		}
	}
	return lowestPoints
}

func bfsBasinsSize(layout [][]int, lowestPoints [][]int) []int {
	visited := make([][]bool, len(layout))
	for i := range visited {
		visited[i] = make([]bool, len(layout[0]))
	}
	var results []int
	for _, lowestPoint := range lowestPoints {
		var queue [][]int
		queue = append(queue, lowestPoint)
		currentResult := 0
		for len(queue) > 0 {
			point := queue[0]
			i := point[0]
			j := point[1]
			if !visited[i][j] {
				if i != 0 && layout[i-1][j] > layout[i][j] && layout[i-1][j] != 9 {
					queue = append(queue, []int{i - 1, j})
				}
				if j != 0 && layout[i][j-1] > layout[i][j] && layout[i][j-1] != 9 {
					queue = append(queue, []int{i, j - 1})
				}
				if i != len(layout)-1 && layout[i+1][j] > layout[i][j] && layout[i+1][j] != 9 {
					queue = append(queue, []int{i + 1, j})
				}
				if j != len(layout[0])-1 && layout[i][j+1] > layout[i][j] && layout[i][j+1] != 9 {
					queue = append(queue, []int{i, j + 1})
				}
				visited[i][j] = true
				currentResult++
			}
			queue = queue[1:]
		}
		results = append(results, currentResult)
	}
	return results
}
