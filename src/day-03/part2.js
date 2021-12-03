const fs = require("fs");
const inputLines = fs.readFileSync("./input.txt", "utf-8").split("\r\n");

const bitsCount = inputLines[0].length;
const tree = buildTree(inputLines);

const oxygenRatingDigits = [];
const co2RatingDigits = [];

let currentVertex = tree;
while (currentVertex.oneCount || currentVertex.zeroCount) {
  let bit;
  if (currentVertex.oneCount && !currentVertex.zeroCount) {
    bit = 1;
  } else if (currentVertex.zeroCount && !currentVertex.oneCount) {
    bit = 0;
  } else {
    bit = currentVertex.oneCount >= currentVertex.zeroCount ? 1 : 0;
  }
  oxygenRatingDigits.push(bit);
  currentVertex = bit == 1 ? currentVertex.oneVertex : currentVertex.zeroVertex;
}

currentVertex = tree;
while (currentVertex.oneCount || currentVertex.zeroCount) {
  let bit;
  if (currentVertex.oneCount && !currentVertex.zeroCount) {
    bit = 1;
  } else if (currentVertex.zeroCount && !currentVertex.oneCount) {
    bit = 0;
  } else {
    bit = currentVertex.zeroCount <= currentVertex.oneCount ? 0 : 1;
  }
  co2RatingDigits.push(bit);
  currentVertex = bit == 1 ? currentVertex.oneVertex : currentVertex.zeroVertex;
}

console.log(
  parseInt(oxygenRatingDigits.join(""), 2) *
    parseInt(co2RatingDigits.join(""), 2)
);

function buildTree(lines) {
  const topVertex = {
    zeroCount: 0,
    oneCount: 0,
    zeroVertex: null,
    oneVertex: null,
  };
  for (const line of inputLines) {
    let currentVertex = topVertex;
    for (let i = 0; i < bitsCount; i++) {
      const symbol = line[i];
      if (symbol == 0) {
        currentVertex.zeroCount = currentVertex.zeroCount + 1 || 1;
        if (!currentVertex.zeroVertex)
          currentVertex.zeroVertex = {
            zeroCount: 0,
            oneCount: 0,
          };
        currentVertex = currentVertex.zeroVertex;
      } else {
        currentVertex.oneCount = currentVertex.oneCount + 1 || 1;
        if (!currentVertex.oneVertex)
          currentVertex.oneVertex = {
            zeroCount: 0,
            oneCount: 0,
          };
        currentVertex = currentVertex.oneVertex;
      }
    }
  }
  return topVertex;
}
