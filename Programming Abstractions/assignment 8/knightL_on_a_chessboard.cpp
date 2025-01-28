#include <bits/stdc++.h>

//problem link: https://www.hackerrank.com/challenges/knightl-on-chessboard/problem

using namespace std;

string ltrim(const string &);
string rtrim(const string &);

/*
 * Complete the 'knightlOnAChessboard' function below.
 *
 * The function is expected to return a 2D_INTEGER_ARRAY.
 * The function accepts INTEGER n as parameter.
 */
 bool in_bounds(int x, int y, int size){
     return min(x,y)>=0 && x<size&& y<size;
 }
 
 void add_friend(queue<pair<int,int>> &q, int i, int j, pair<int,int> now, int n, vector<vector<int>> &used, vector<vector<int>> &distance){
     if(!in_bounds(i,j,n)||used[i][j]) return;
     used[i][j]=1;
     distance[i][j] = distance[now.first][now.second]+1;
     q.push({i,j});
 }
 
void bfs(vector<vector<int>> &answer, int n, int x, int y){
    vector<int> temp(n,0);
    vector<vector<int>> used(n, temp);
    vector<vector<int>> distance(n, temp);
    for(int i=0; i<n; i++){
        vector<int> temp;
        for(int j=0; j<n; j++){
            temp.push_back(0);
        }
        distance.push_back(temp);
        used.push_back(temp);
        
    }
    queue<pair<int,int>> q;
    pair<int, int> start = {0,0};
    q.push(start);
    used[0][0]=1;
    while(!q.empty()){
        pair<int,int> now = q.front();
        q.pop();
        if(now.first==n-1&&now.second==n-1){
            answer[x-1][y-1]=distance[n-1][n-1];
            return;
        }
        add_friend(q, now.first+x, now.second+y, now, n, used, distance);
        add_friend(q, now.first+y, now.second+x, now, n, used, distance);
        add_friend(q, now.first+x, now.second-y, now, n, used, distance);
        add_friend(q, now.first+y, now.second-x, now, n, used, distance);
        add_friend(q, now.first-x, now.second+y, now, n, used, distance);
        add_friend(q, now.first-y, now.second+x, now, n, used, distance);
        add_friend(q, now.first-x, now.second-y, now, n, used, distance);
        add_friend(q, now.first-y, now.second-x, now, n, used, distance);
    }
    answer[x-1][y-1] = -1;
}

vector<vector<int>> knightlOnAChessboard(int n) {
    vector<int> temp(n-1);
    vector<vector<int>> answer(n-1, temp);
    for(int x=1; x<n;x++){
        for(int y=1; y<n; y++){
            bfs(answer, n, x, y);        
        }
    }
    return answer;
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string n_temp;
    getline(cin, n_temp);

    int n = stoi(ltrim(rtrim(n_temp)));

    vector<vector<int>> result = knightlOnAChessboard(n);

    for (size_t i = 0; i < result.size(); i++) {
        for (size_t j = 0; j < result[i].size(); j++) {
            fout << result[i][j];

            if (j != result[i].size() - 1) {
                fout << " ";
            }
        }

        if (i != result.size() - 1) {
            fout << "\n";
        }
    }

    fout << "\n";

    fout.close();

    return 0;
}

string ltrim(const string &str) {
    string s(str);

    s.erase(
        s.begin(),
        find_if(s.begin(), s.end(), not1(ptr_fun<int, int>(isspace)))
    );

    return s;
}

string rtrim(const string &str) {
    string s(str);

    s.erase(
        find_if(s.rbegin(), s.rend(), not1(ptr_fun<int, int>(isspace))).base(),
        s.end()
    );

    return s;
}

