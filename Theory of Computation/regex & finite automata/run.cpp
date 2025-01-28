#include <bits/stdc++.h>
#define ll long long
#define speeder ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);
#define pci pair<char, int>
#define pb push_back
using namespace std;

//s aris input
//n aris mdgomareobebis raodenoba
//a aris mimghebebis raodenoba
//t aris gadasvlebis raodenoba

string s;
int n, a, t, x, sz;
char ch;


void fillData(vector<bool> &states, vector<vector<pci>> &adj){
	for(int i = 0; i < a; i++){
		cin >> x;
		states[x] = true;
	}
	for(int i = 0; i < n; i++){
		cin >> sz;
		for(int j = 0; j < sz; j++){
			cin >> ch >> x;
			adj[i].pb({ch, x});
		}
	}
}

void print(vector<bool> &states, vector<vector<pci>> &adj){
	cout << n << " " << a << " " << t << endl;
	for(int i = 0; i < n; i++){
		if(states[i]) cout << i << " ";
	}
	cout << endl;
	for(int i = 0; i < n; i++){
		cout << adj[i].size() << " ";
		for(int j = 0; j < adj[i].size(); j++) {
			cout << adj[i][j].first << " " << adj[i][j].second << " ";
		}
		cout << endl;
	}
}

string bfs(vector<bool> &states, vector<vector<pci>> &adj){
	string res = "";
	queue<int> q;
	int index = 0;
	q.push(0);
	while(!q.empty() && index < s.length()){
		bool check = false;
		int size = q.size();
		for(int i = 0; i < size; i++){
			int curr = q.front();
			q.pop();
			for(int j = 0; j < adj[curr].size(); j++){
				pci next = adj[curr][j];
				if(next.first == s[index]){
					check = check || states[next.second];
					q.push(next.second);	
				}
			}
		}
		if(check){
			res += "Y";
		}else{
			res += "N";
		}
		index++;
	}
	for(int i = index; i < s.length(); i++){
		res += "N";
	}
	return res;	
}

void solve(){
	cin >> s >> n >> a >> t;
	vector<bool> states(n, false);
	vector<vector<pci>> adj(n);
	fillData(states, adj);
	cout << bfs(states, adj) << endl;
}

int main(){
	speeder;
	int t = 1;
	//cin>>t;
	while(t--){
		solve();
	}
	return 0;
}


