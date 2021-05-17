import sys
filepath = sys.argv[1]
lineCount = 0
wordCount = 0

f = open(filepath, 'r')

for line in f:
    # 마지막 줄은 count 하지 않음
    if len(line)!=1:
        lineCount += 1
        words = line.split()
        wordCount += len(words)

print("# of line : {0:d}".format(lineCount))
print("# of word : {0:d}".format(wordCount))

f.close()