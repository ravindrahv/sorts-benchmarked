# sorts-benchmarked

A collection of **benchmarked sorting algorithms** (some re-discovered and some novel/original), evaluated using **Java Microbenchmark Harness (JMH)**.

This repository exists mainly to document and share a key observation:

> There exists an **in-place, stable, and parallel-friendly sorting algorithm** (Combine-Sort), but it is not formally published in peer-reviewed literature.

---

## Algorithms

### 1) Combine-Sort (original / novel)
**Combine-Sort** is an original sorting algorithm (as per my understanding) with the following characteristics:

- ✅ In-place
- ✅ Stable
- ✅ Parallel-friendly
- ⏱️ Time complexity: **O(n · log² n)** (i.e., *O(n · log(n) · log(n))*)
- 🗓️ Originally created around **2011**

More details:
- https://ravindrahv.wordpress.com/2025/08/01/combinesort-a-stable-in-place-parallel-friendly-sorting-algorithm-with-on-log²-n-complexity/

---

### 2) Virtual-Pivot QuickSort (variation / re-discovered)
This is a QuickSort variation where pivots are **not selected from within the data**.  
Instead, the pivot is computed from the current value range:

> `pivot = (min + max) / 2`

Where `min` and `max` are obtained by scanning the current partition.

Intuition / notes:

- This behaves more like a **range-based partitioning scheme** and has similarities to **radix-style sorting**, since the pivot is derived from value range rather than sampling.
- One possible worst-case input resembles a powers-of-two distribution:
  - `1, 2, 4, 8, 16, ...`
- Because the pivot is computed from the numeric range, the recursion depth is bounded by the datatype bit-width (≈31 for signed 32-bit ints). After ~log₂(max−min) range splits, further pivot refinement converges, making the approach resemble MSD radix-style range partitioning and avoiding quicksort’s classic n² worst case.
- This suggests a practical worst-case within **O(n · log(n))**, with the additional cost of scanning for `min/max` at each iteration.

More details:
- https://ravindrahv.wordpress.com/2013/03/24/quick-sort-simple-averaged-virtual-pivot-algorithm/

---

### 3) In-place Counting MSD Radix Hybrid (American Flag Sort)
The **in-place counting MSD radix hybrid** corresponds to the well-known **American Flag Sort** family.

This was a rediscovery on my part, and it appears others have independently rediscovered similar variants.

Reference:
- https://duvanenko.tech.blog/2022/04/10/in-place-n-bit-radix-sort/

---

## Publication status & implementation notes

- Combine-Sort is **not (yet) published** in any peer-reviewed journal.
- The same is true for the Virtual-Pivot QuickSort variant.
- The implementations provided are **proof-of-concept** and may contain edge-case bugs, although care has been taken to validate correctness.

---

## Benchmark highlights

Based on the included benchmark logs:

- The **parallel-friendly Combine-Sort** performs **on par with Java’s `Arrays.sort()`** (non-parallel standard library sort) for random input sets.
- The **in-place MSD radix hybrid / American Flag Sort** becomes particularly competitive, and can be faster for large random arrays (notably **above ~10 million elements**).

Refer to the benchmark logs for full details and measurements.


---
---
---


## Theory / rationale (Virtual-Pivot QuickSort)

Classic QuickSort chooses a pivot from within the data. If the pivot choices are consistently bad (which can happen for certain input orders), QuickSort can degrade to very slow performance: **O(n²)**.

This variant avoids that problem by **not selecting pivots from the data at all**.  
Instead, on each step it computes a *virtual pivot* using only the current numeric range:

> `pivot = (min + max) / 2`

where `min` and `max` are the minimum and maximum values in the current partition.

### Why this helps
For fixed-width integer datatypes (example: Java `int` is 32-bit signed), the number of times we can keep halving the value range is limited:

- A 32-bit signed `int` has about **31 meaningful value bits** in the positive range.
- Each step halves the numeric interval.
- After ~31 halvings, the range becomes too narrow to keep producing new pivots.

So the recursion depth is capped by the datatype’s **bit-width**, not by the number of elements.

### Intuition
This makes the algorithm behave more like **range partitioning / radix-style partitioning**, and it avoids QuickSort’s typical worst-case pitfall (deep recursion due to bad pivots).


### Summary
**Key idea:** the pivot is chosen from the **numeric range** instead of the array.  
Because integers have a fixed bit-width, the pivot refinement depth is bounded (≈31 levels for Java `int`), preventing QuickSort’s typical \(O(n^2)\) worst case and making it resemble MSD radix / range partitioning.
