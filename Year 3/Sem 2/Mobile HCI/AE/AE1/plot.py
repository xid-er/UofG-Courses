import matplotlib.pyplot as plt
from matplotlib import colors
import numpy as np

data = np.loadtxt("data.csv", delimiter=',')
data = np.rot90(data, k=1, axes=(0, 1))

# create discrete colormap
cmap = colors.ListedColormap(['white', 'black'])
bounds = [0,1]
norm = colors.BoundaryNorm(bounds, cmap.N)

fig, ax = plt.subplots()
# draw boxes
ax.imshow(data, cmap=cmap, norm=norm, aspect="auto")

names = ['Mahnoor', 'Lewis', 'Paul', 'Sarahi', 'Kyle', 'Rishabh', '']

ax.set_xticks(np.arange(0, 21, 1), minor=False);
ax.set_yticklabels(names[::-1], minor=False);

# draw lines
[ax.axhline(y=i+0.5, linestyle='-', color="k") for i in range(6)]
[ax.axvline(x=i+0.5, linestyle='-', color="k") for i in range(21)]

plt.xlabel("Usability Problem")
plt.ylabel("Evaluator")

ax.set_aspect('auto')

plt.show()
