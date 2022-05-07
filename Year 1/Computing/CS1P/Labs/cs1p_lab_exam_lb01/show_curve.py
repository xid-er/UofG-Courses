import matplotlib.pyplot as plt
import numpy as np


def show_curve(curve_fn, parameters, data):
    data = np.array(data)
    plt.plot(data[:, 0], data[:, 1], "C1o", label="Data")
    mn, mx = np.min(data[:, 0]), np.max(data[:, 0])
    xs = np.linspace(mn, mx, 200)
    plt.plot(xs, [curve_fn(parameters, x) for x in xs], label="Curve")
    plt.legend()
    plt.title(f"Curve fit")


def show_many_curves(curve_fn, data, fits, slices=None):
    data = np.array(data)
    plt.plot(data[:, 0], data[:, 1], "C1o", label="Data")
    if slices is None:
        slices = [(0, len(data)) for i in range(len(fits))]
    for fit, (start, end) in zip(fits, slices):
        if end - start > 0:
            mn, mx = np.min(data[start:end, 0]), np.max(data[start:end, 0])
            xs = np.linspace(mn, mx, 200)

            plt.plot(xs, [curve_fn(fit, x) for x in xs], c="C2", alpha=0.2, lw=2)

    plt.title(f"Partial curve fits")

