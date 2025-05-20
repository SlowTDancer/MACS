#include <bits/stdc++.h>

using namespace std;

int task_the_bridge(vector<int> &times){
	int ans = 0, temp1, temp2;
	if(times.size() == 1) return times[0];
	int count = times.size();
	while(count >= 4){
		temp1 = 2*times[0] + times[count - 1] + times[count - 2];
		temp2 = times[0] + 2*times[1] + times[count - 1];
		ans = ans + min(temp1, temp2);
		count = count - 2;
	}
	if(count == 3){
		ans = ans + times[0] + times[1] + times[2];
	}
	if(count == 2){
		ans = ans + times[1];
	}
	return ans;
}

int main() {
	int n, x;
	vector<int> times;
	scanf("%i", &n);
	for (int i = 0; i < n; i++) {
		scanf("%i", &x);
		times.push_back(x);
	}
	int answer = task_the_bridge(times);
	printf("%i \n", answer);
	return 0;
}