# Solution to Advent Of Code 2021, Day 10.
#

# Reading input lines.
lines = open('prod_input.txt').read().split('\n')

# Mapping opening parenthesis to closing parenthesis.
couple = {
    ')': '(',
    ']': '[',
    '}': '{',
    '>': '<'}
opens = set(couple.values())

# Score for corrupted symbols.
corruption_score_map = {
    ')': 3,
    ']': 57,
    '}': 1197,
    '>': 25137}

# Score for completion of each symbol.
completion_score_map = {
    '(': 1,
    '[': 2,
    '{': 3,
    '<': 4}

corruption_total_score = 0
completion_scores = []

for line in lines:
  state = []
  for c in line:
    if c in opens:
      state.append(c)
    else:  # We have a closing parenthesis.
      if len(state) > 0 and state[-1] == couple[c]:
        state.pop()
      else:
        # Corrupting parenthesis found. 
        corruption_total_score += corruption_score_map[c]
        break
  else:
    completion_score = 0
    for s in reversed(state):
      completion_score = completion_score * 5 + completion_score_map[s]
    completion_scores.append(completion_score)

def Middle(l):
  return list(sorted(l))[len(l) // 2]

print('Total corruption score:', corruption_total_score)
print('Total completion score:', Middle(completion_scores))


