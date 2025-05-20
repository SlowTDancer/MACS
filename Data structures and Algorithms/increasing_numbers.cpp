#include <bits/stdc++.h>
#define speeder ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);;

using namespace std;

int fenwick[1000001];

void clear(){
	for(int i = 0; i < 1000001; i++){
		fenwick[i] = 0;
	}
}

void update(int index, int value){
	while(index <= 1000001){
		if(value <= fenwick[index]) break;
		fenwick[index] = value;
		if(index == 0) break;
		index = index + (index & (-index));
	}
}

int sum(int index){
	int answer = 0;
	while(index >= 0){
		answer = max(answer, fenwick[index]);
		if(index == 0) break;
		index = index - (index & (-index));	
	}
	return answer;
}

void find_answer(int &answer, int L){
	for(int i = 0; i < 1000001; i++){
		if(fenwick[i] >= L) {
			answer = i;
			break;
		}
	}
}

int solve(vector<int> &nums, int L){
    int answer = -1;
    if(L <= 0) return -1;
	for(int i = 0; i < nums.size(); i++){
    	int curr = sum(nums[i] - 1);
    	curr++;
    	update(nums[i], curr);
	}
	find_answer(answer, L);
    return answer;
}

int main(){
    speeder;
    int T, n, x, L;
    cin >> T;
    for(int j = 0; j < T; j++){
        vector<int> nums;
        cin >> n;
        for(int i = 0; i < n; i++){
            cin >> x;
            nums.push_back(x);
        }
        cin >> L;
        int answer = solve(nums, L);
        clear();
        cout << answer << endl;
    }
    return 0;
}
