import os

import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns


def generate_graph(csv_path, output_path, title):
    # Read CSV
    try:
        df = pd.read_csv(csv_path)
    except FileNotFoundError:
        print(f"Error: Could not find file {csv_path}")
        return

    # Create the plot
    plt.figure(figsize=(12, 7))
    sns.set_theme(style="whitegrid", context="paper", font_scale=1.2)

    # Plot lines with some style
    sns.lineplot(
        data=df,
        x="generation",
        y="max_score",
        label="Max Score",
        color="#2ecc71",
        linewidth=2.5,
    )
    sns.lineplot(
        data=df,
        x="generation",
        y="average_score",
        label="Average Score",
        color="#3498db",
        linewidth=2.5,
    )
    sns.lineplot(
        data=df,
        x="generation",
        y="min_score",
        label="Min Score",
        color="#e74c3c",
        linewidth=1.5,
        alpha=0.7,
        linestyle="--",
    )

    # Customize the graph
    plt.title(title, fontsize=16, fontweight="bold", pad=20)
    plt.xlabel("Generation", fontsize=12)
    plt.ylabel("Score", fontsize=12)
    plt.legend(title="Metrics", title_fontsize=12)

    # Add a fill between min and max to show the range
    plt.fill_between(df["generation"], df["min_score"], df["max_score"], color="#3498db", alpha=0.1)

    # Save the plot
    plt.tight_layout()
    plt.savefig(output_path, dpi=300, bbox_inches="tight")
    plt.close()
    print(f"Generated graph: {output_path}")


def main():
    # Define paths relative to this script
    base_dir = os.path.dirname(os.path.abspath(__file__))
    chromosomes_dir = os.path.join(base_dir, "../chromosomes")
    output_dir = os.path.join(base_dir, "..")  # Save to root where report.tex is

    # Graph 1: Basic GA Extra Training
    generate_graph(
        os.path.join(chromosomes_dir, "extra-training-results.csv"),
        os.path.join(output_dir, "extra_training.png"),
        "Score Evolution (Basic GA - Extra Training)",
    )

    # Graph 2: Improved Algorithm Extra Training
    generate_graph(
        os.path.join(chromosomes_dir, "improved-algorithm-extra-training-results.csv"),
        os.path.join(output_dir, "final_success.png"),
        "Score Evolution (Improved Algorithm - Extra Training)",
    )


if __name__ == "__main__":
    main()
