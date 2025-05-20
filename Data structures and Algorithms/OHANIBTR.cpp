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

void find_answer(int &answer, int size){
	for(int i = 0; i <= size; i++){
		if(fenwick[i] > answer) answer = fenwick[i];
	}
	clear();
}

int lis(vector<int> &nums){
	int answer = -1;
	for(int i = 0; i < nums.size(); i++){
    	int curr = sum(nums[i] - 1);
    	curr++;
    	update(nums[i], curr);
	}
	find_answer(answer, nums.size());
    return answer;
}

void write_answer(vector<int> &nums, int T, int answer){
	cout << "Case " << T <<  ":" << endl;
    cout << "Minimum Move: " << answer << endl;
    for(int i = 0; i < nums.size(); i++){
        cout << nums[i];
        if(i != nums.size() - 1) cout << " ";
    }
    cout << endl;
}

void ohani_sort(vector<int> &nums, int &answer){
	answer = nums.size() - lis(nums);
}

int find_key(int size){
	if(size == 1) return 1;
	int answer = 0;
	int pow2 = 1;
	int sum = 0;
	while(sum + pow2 < size){
		sum = sum + pow2;
		pow2 = pow2 * 2;
	}
	pow2 = pow2 / 2;
	int leftovers = size - sum;
	if(leftovers >= pow2){
		answer =  2 * pow2;
	}else{
		answer = pow2 + leftovers;
	}
	return answer;
}

void find_parents(vector<int> &nums, int left, int right, int par){
	if(left > right) return;
	int key = left - 1 + find_key(right - left + 1);
	nums[key - 1] = par;
	find_parents(nums, left, key - 1, key);
	find_parents(nums, key + 1, right, key);
}

void solve(vector<int> &nums, int T){
    int answer = 0;
    ohani_sort(nums, answer);
    find_parents(nums, 1, nums.size(), -1);
    write_answer(nums, T, answer);
}

int main(){
    speeder;
    int T, n, x;
    cin >> T;
    for(int i = 0; i < T; i++){
        cin  >> n;
        vector<int> nums;
        for(int j = 0; j < n; j++){
            cin >> x;
            nums.push_back(x);
        }
        solve(nums, i + 1);
    }
    return 0;
}
