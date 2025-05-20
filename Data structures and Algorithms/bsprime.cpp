#include <bits/stdc++.h>

using namespace std;

int k = 0, j, num[101865032], pnum[6000000], temp, all, ones;

void fill_data(int i, vector<pair<int, int>>& data) {
    pair<int, int> p;
    ones = __builtin_popcount(i);
    all = (int)log2(i) + 1;
    p.first = data[data.size() - 1].first + all;
    p.second = data[data.size() - 1].second + ones;
    data.push_back(p);
}

void eratosthenes_sieve(vector<pair<int, int>>& data) {
    data.push_back({0, 0});
    for (int i = 2; i <= 101865031; i++) {
        if (num[i] == 0) {
            num[i] = i;
            pnum[k++] = i;
            fill_data(i, data);
        }
        for (j = 0; ; j++) {
            if ((j == k) || ((i * pnum[j]) > 101865031) || (pnum[j] > num[i])) break;
            num[i * pnum[j]] = pnum[j];
        }
    }
}

int remain(int p, int limit){
    int answer = 0;
    vector<int> to_bit;
    if(limit == 0) return answer;
    while(p != 0){
        to_bit.push_back(p%2);
        p = p/2;
    }
    int now = 0;
    for(int i = 0; i < to_bit.size(); i++){
        if(limit == now) break;
        answer = answer + to_bit[to_bit.size() - 1 - i];
        now++;
    }
    return answer;
}

int bsprime(vector<pair<int, int>> &data, int N){
    pair<int, int> temp = {N, INT_MAX};
    int index = upper_bound(data.begin(), data.end(), temp) - data.begin() - 1;
    return data[index].second + remain(pnum[index-1], N - data[index].first);
}

int main() {
    int t, N, answer;
    scanf("%i", &t);
    vector<pair<int, int>> data;
    data.push_back({0, 0});
    eratosthenes_sieve(data);
    for (int i = 0; i < t; i++) {
        scanf("%i", &N);
        answer = bsprime(data, N);
        printf("%i\n", answer);
    }
    return 0;
}