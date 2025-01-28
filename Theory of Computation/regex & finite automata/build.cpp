#include <bits/stdc++.h>
#define ll long long
#define speeder ios_base::sync_with_stdio(false); cin.tie(NULL); cout.tie(NULL);
#define pwb pair<vector<pair<char, int>>, bool>
#define ways first
#define accept second
#define pb push_back
using namespace std;

string inputS;


bool isSymbol(char ch){
	return ('a' <= ch && ch <= 'z') || ('0' <= ch && ch <= '9') || ch == '$';
}

int priority (char alpha){
    if(alpha == '|') return 1;
 	if(alpha == '.') return 2;
 	if(alpha == '*') return 3;
	return 0;
}

string haru(string s){
	string res = "";
	string temp = "";
	for(int i = 0; i < s.length(); i++){
		if(i < s.length() - 1 && s[i] == '(' && s[i + 1] == ')'){
			temp += "$";
			i++;
		}else{
			temp += s[i];
		}
	}
	s = temp;
	char ch = s[0];
	res += ch;
	for(int i = 1; i < s.length(); i++){
		if((isSymbol(ch) && isSymbol(s[i])) || (ch == '*' && (isSymbol(s[i]) || s[i] == '(')) || (isSymbol(ch) && s[i] == '(')
		|| (ch == ')' && s[i] == '(') || (ch == ')' && isSymbol(s[i]))){
			res += '.';
		}
		res += s[i];
		ch = s[i];
	}
	return res;
}

string analyzeInput(string s){
    string res = "";
    stack<int> st;
    for(int i = 0; i < s.length(); i++){
        char ch = s[i];
		if(isSymbol(ch)){
			res += ch;
        }else if(ch == '('){
			st.push(ch);
		}else if(ch == ')'){
            while(st.top() != '('){
                res += st.top();
                st.pop();
            }
            st.pop();
        }else{
            while(!st.empty() && priority(ch) <= priority(st.top())){
                res += st.top();
                st.pop();
            }
            st.push(ch);
        }
    }
    while(!st.empty()){
        res += st.top();
        st.pop();
    }
	return res;
}

void addEpsilon(stack<vector<pwb>> &st){
	vector<pwb> res;
	pwb curr;
	curr.accept = true;
	res.pb(curr);
	st.push(res);
}

void addState(stack<vector<pwb>> &st, char ch){
	vector<pwb> res;
	pwb firstState, secondState;
	firstState.accept = false;
	firstState.ways.pb({ch, 1});
	res.pb(firstState);
	secondState.accept = true;
	res.pb(secondState);
	st.push(res);
}

void concatenate(stack<vector<pwb>> &st){
	vector<pwb> second = st.top();
	st.pop();
	vector<pwb> first = st.top();
	st.pop();
	for(int i = 0; i < second.size(); i++){
		for(int j = 0; j < second[i].ways.size(); j++){
			second[i].ways[j].second += first.size() - 1;
		}
	}
	for(int i = 0; i < first.size(); i++){
		if(!first[i].accept) continue;
		first[i].accept = second[0].accept;
		for(int j = 0; j < second[0].ways.size(); j++){
			first[i].ways.pb(second[0].ways[j]);
		}
	}
	for(int i = 1; i < second.size(); i++){
		first.pb(second[i]);
	}
	st.push(first);
}

void star(stack<vector<pwb>> &st){
	vector<pwb> curr = st.top();
	st.pop();
	curr[0].accept = true;
	for(int i = 1; i < curr.size(); i++){
		if(!curr[i].accept) continue;
		for(int j = 0; j < curr[0].ways.size(); j++){
			curr[i].ways.pb(curr[0].ways[j]);
		}
	}
	st.push(curr);
}

void Union(stack<vector<pwb>> &st){
	vector<pwb> second = st.top();
	st.pop();
	vector<pwb> first = st.top();
	st.pop();
	vector<pwb> res;
	pwb start;
	start.accept = first[0].accept || second[0].accept;
	for(int i = 0; i < first[0].ways.size(); i++){
		start.ways.pb(first[0].ways[i]);
	}
	for(int i = 0; i < second[0].ways.size(); i++){
		if(second[0].ways[i].second) second[0].ways[i].second += first.size() - 1;
		start.ways.pb(second[0].ways[i]);
	}
	res.pb(start);
	for(int i = 1; i < first.size(); i++){
		res.pb(first[i]);
	}
	for(int i = 1; i < second.size(); i++){
		for(int j = 0; j < second[i].ways.size(); j++){
			if(second[i].ways[j].second) second[i].ways[j].second += first.size() - 1;
		}
	}
	for(int i = 1; i < second.size(); i++){
		res.pb(second[i]);
	}
	st.push(res);
}

string writeFirstLine(vector<pwb> &nfa, int &as){
	string res = "";
	res += to_string(nfa.size()) + " ";
	int acceptStates = 0;
	for(int i = 0; i < nfa.size(); i++){
		if(nfa[i].accept) acceptStates++;
	}
	as = acceptStates;
	res += to_string(acceptStates) + " ";
	int waysCounter = 0;
	for(int i = 0; i < nfa.size(); i++){
		waysCounter += nfa[i].ways.size();
	}
	res += to_string(waysCounter) + "\n";
	return res;
}

string writeSecondLine(vector<pwb> &nfa, int &as){
	string res = "";
	int counter = 0;
	for(int i = 0; i < nfa.size(); i++){
		if(!nfa[i].accept) continue;
		res += to_string(i);
		counter++;
		if(as != counter) res += " ";
	}
	res += "\n";
	return res;	
}

string convert(vector<pwb> nfa){
	int as;
	string res = writeFirstLine(nfa, as);
	res += writeSecondLine(nfa, as);
	for(int i = 0; i < nfa.size(); i++){
		int sz = nfa[i].ways.size();
		res += to_string(sz);
		for(int j = 0; j < sz; j++){
			res += " ";
			res += nfa[i].ways[j].first;
			res += " ";
			res += to_string(nfa[i].ways[j].second);
		}
		res += "\n";
	}
	return res;
}


string NFAsolver(string s){
	stack<vector<pwb>> st;
	int index = 0;
	while(index < s.length()){
		if(s[index] == '$'){
			addEpsilon(st);
		}else if(isSymbol(s[index])){
			addState(st, s[index]);
		}else if(s[index] == '.'){
			concatenate(st);
		}else if(s[index] == '*'){
			star(st);
		}else{
			Union(st);
		}
		index++;
	}
	string res = convert(st.top());
	return res;
}

void solve(){
	cin >> inputS;
	string temp = haru(inputS);
	string str = analyzeInput(temp);
	string res = NFAsolver(str);
	cout << res << endl;
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
