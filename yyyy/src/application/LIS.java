package application;

public class LIS {
//exted data in one operation 
	private static class LISData {
		int[] tails;//this index tails
		int[] tailsIndices;//store number of tails
		int[] prevIndices;//store previous index 
		int[] dp; // dp[i] = LIS length ending at L[i]
		int len;
	}

	private LISData compute(int[] L) {
		// call arrays
		int n = L.length;
		LISData d = new LISData();
		d.tails = new int[n];
		d.tailsIndices = new int[n];
		d.prevIndices = new int[n];
		d.dp = new int[n];
		d.len = 0;

		for (int i = 0; i < n; i++) {

			int pos = binarySearch(d.tails, d.len, L[i]);
			// put number in place **last number is greater
			d.tails[pos] = L[i];
			// save indices for this number
			d.tailsIndices[pos] = i;

			if (pos > 0) {
				// save prev number to now way number get
				d.prevIndices[i] = d.tailsIndices[pos - 1];
			} else {
				// first number come
				d.prevIndices[i] = -1;
			}
			// save number to d why increeze 1 even not started 0
			d.dp[i] = pos + 1;

			if (pos == d.len) {

				d.len++;
			} else {
			}
		}
		return d;
	}

	public int[] getLISValues(int[] L) {
		LISData d = compute(L);
		int[] result = new int[d.len];
		int curr = d.tailsIndices[d.len - 1];
		for (int i = d.len - 1; i >= 0; i--) {
			result[i] = L[curr];
			curr = d.prevIndices[curr];
		}
		return result;
	}
//indices is lamp back treasor
	public int[] indexValueForTabel(int[] L) {
		LISData d = compute(L);
		int[] idx = new int[d.len];
		int curr = d.tailsIndices[d.len - 1];
		for (int i = d.len - 1; i >= 0; i--) {
			idx[i] = curr;
			curr = d.prevIndices[curr];
		}
		return idx;
	}

	public int[] getDPTable(int[] L) {
		return compute(L).dp;
	}

	private int binarySearch(int[] tails, int len, int target) {
		int low = 0, high = len - 1, pos = len;
		while (low <= high) {
			int mid = low + (high - low) / 2;
			if (tails[mid] >= target) {
				pos = mid;
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		return pos;
	}
}