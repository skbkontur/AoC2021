const fs = require("fs");
const inputLines = fs.readFileSync("./input.txt", "utf-8").split("\r\n");

const bitsCount = inputLines[0].length;
let gammaRate = 0;
for (let i = 0; i < bitsCount; i++) {
  const maxBit = getMaxBit(inputLines, i);
  gammaRate = gammaRate + maxBit * Math.pow(2, bitsCount - i - 1);
}
const epsilonRate = gammaRate ^ ((1 << bitsCount) - 1);
console.log(gammaRate * epsilonRate);

function getMaxBit(lines, position) {
  const numberOfOnes = lines.reduce(
    (prev, curr) => (curr[position] == 1 ? prev + 1 : prev),
    0
  );
  const numberOfZeroes = lines.length - numberOfOnes;
  return numberOfOnes > numberOfZeroes ? 1 : 0;
}
