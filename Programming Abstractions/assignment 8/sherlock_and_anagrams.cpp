#include <bits/stdc++.h>

//problem link: https://www.hackerrank.com/challenges/sherlock-and-anagrams/problem

using namespace std;

string ltrim(const string &);
string rtrim(const string &);

/*
 * Complete the 'sherlockAndAnagrams' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts STRING s as parameter.
 */
 string asort(string a){
     string answer = "";
     vector<char> temp;
     for(int i=0; i<a.size(); i++){
         temp.push_back(a[i]);
     }
     sort(temp.begin(), temp.end());
     for(int i=0; i<temp.size(); i++){
         answer = answer + temp[i];
     }
     return answer;
 }

int sherlockAndAnagrams(string s) {
    int count = 0;
    map<string, int> data;
    for(int i=0; i<s.size(); i++){
        for(int j=1; j<s.size()-i+1; j++){
            data[asort(s.substr(i, j))]++;
        }
    }
    for(auto x : data){
        count += x.second*(x.second-1)/2;
    }
    return count;
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string q_temp;
    getline(cin, q_temp);

    int q = stoi(ltrim(rtrim(q_temp)));

    for (int q_itr = 0; q_itr < q; q_itr++) {
        string s;
        getline(cin, s);

        int result = sherlockAndAnagrams(s);

        fout << result << "\n";
    }

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

