/**
 * Function parses line "dfgabce cadfgb cefa ca aecbg dfcegb geabd ecbfg cab agcfbe | egbfadc dbgae gcfeb abgdfc"
 * to pattern and output devided by '|', where pattern and output are arrays of strings.
 */
function parseInput(input) {
  const pattern = input.split(" | ")[0].split(" ")
  const output = input.split(" | ")[1].split(" ")
  return { pattern, output }
}

// Function treat input as list of lines and parses each line to pattern and output.
function parseInputToPatterns(input) {
  return input.split("\n").map(parseInput)
}

/**
 * Function
 *  parses input
 *  iterates over the outputs in every line
 *  return the count of entries that have length 2, 3, 4 or 7
 *
 * @param {string} input
 */
function solvePart1(input) {
  const patterns = parseInputToPatterns(input)
  let count = 0
  patterns.forEach(({ pattern, output }) => {
    output.forEach((word) => {
      if (
        word.length === 2 ||
        word.length === 3 ||
        word.length === 4 ||
        word.length === 7
      ) {
        count++
      }
    })
  })
  return count
}

/**
 * @param {string} input
 */
function solvePart2(input) {
  const patterns = parseInputToPatterns(input)

  let sum = 0
  patterns.forEach(({ pattern, output }) => {
    const one = pattern.find((segments) => segments.length === 2)
    const four = pattern.find((segments) => segments.length === 4)
    const seven = pattern.find((segments) => segments.length === 3)
    const eight = pattern.find((segments) => segments.length === 7)

    const candidatesForSix = pattern.filter((segments) => segments.length === 6)
    const six = candidatesForSix.find((pattern) => {
      return ![...one].every((segment) => pattern.includes(segment))
    })

    const zero = candidatesForSix
      .filter((pattern) => pattern !== six)
      .find((pattern) => {
        return ![...four].every((segment) => pattern.includes(segment))
      })

    const nine = candidatesForSix.find(
      (pattern) => pattern !== six && pattern !== zero,
    )

    const candidatesForThree = pattern.filter(
      (segments) => segments.length === 5,
    )
    const three = candidatesForThree.find((pattern) => {
      return [...one].every((segment) => pattern.includes(segment))
    })

    const five = candidatesForThree.find((pattern) => {
      return [...pattern].every((segment) => six.includes(segment))
    })

    const two = candidatesForThree.find((pattern) => {
      return pattern !== three && pattern !== five
    })

    const sortStr = (str) => str.split("").sort().join("")

    const patternsToNumbers = {
      [sortStr(one)]: 1,
      [sortStr(two)]: 2,
      [sortStr(three)]: 3,
      [sortStr(four)]: 4,
      [sortStr(five)]: 5,
      [sortStr(six)]: 6,
      [sortStr(seven)]: 7,
      [sortStr(eight)]: 8,
      [sortStr(nine)]: 9,
      [sortStr(zero)]: 0,
    }

    const outputNumber = output
      .map((pattern) => {
        return patternsToNumbers[sortStr(pattern)]
      })
      .join("")

    sum += parseInt(outputNumber, 10)
  })

  return sum
}

function main() {
  const input = require('fs').readFileSync('./08.txt', 'utf8')
  console.log("Part 1:", solvePart1(input))
  console.log("Part 2:", solvePart2(input))
}

main()
