#include <bits/stdc++.h>

//problem link: https://www.hackerrank.com/challenges/reduced-string/problem

using namespace std;

/*
 * Complete the 'superReducedString' function below.
 *
 * The function is expected to return a STRING.
 * The function accepts STRING s as parameter.
 */

string superReducedString(string s) {
    for(int i=0; i<s.size()-1; i++){
        if(s[i]==s[i+1]){
            if(s.size()==2){
                s = "Empty String";
                break;
            }
            s = s.substr(0, i)+s.substr(i+2);
            i = -1;
        }
    }
    return s;
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string s;
    getline(cin, s);

    string result = superReducedString(s);

    fout << result << "\n";

    fout.close();

    return 0;
}

