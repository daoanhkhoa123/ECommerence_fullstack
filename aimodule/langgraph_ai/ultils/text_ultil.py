from typing import List, Tuple
import heapq

def edit_distance(a: str, b: str) -> int:
    m, n = len(a), len(b)
    prev = list(range(n + 1))
    curr = [0] * (n + 1)
    for i in range(1, m + 1):
        curr[0] = i
        for j in range(1, n + 1):
            curr[j] = min(
                prev[j] + 1,
                curr[j - 1] + 1,
                prev[j - 1] + (0 if a[i - 1] == b[j - 1] else 1)
            )
        prev, curr = curr, prev
    return prev[n]

def top_k_matches_editdistance(query: str, docs: List[str], k: int) -> List[int]:
    heap = []
    for idx, doc in enumerate(docs):
        dist = edit_distance(query, str(doc))
        if len(heap) < k:
            heapq.heappush(heap, (-dist, idx))
        else:
            if dist < -heap[0][0]:
                heapq.heappushpop(heap, (-dist, idx))
    return [idx for _, idx in heap]
