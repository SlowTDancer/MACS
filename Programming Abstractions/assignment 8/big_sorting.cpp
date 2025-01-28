#include <bits/stdc++.h>

//problem link: https://www.hackerrank.com/challenges/big-sorting/problem

using namespace std;

string ltrim(const string &);
string rtrim(const string &);

/*
 * Complete the 'bigSorting' function below.
 *
 * The function is expected to return a STRING_ARRAY.
 * The function accepts STRING_ARRAY unsorted as parameter.
 */
 bool check(string a, string b){
     if(a.size()>b.size()) return false;
     if(a.size()<b.size()) return true;
     for(int i=0;i<a.size(); i++){
         if((a[i]-'0')>(b[i]-'0')) return false;
         if((a[i]-'0')<(b[i]-'0')) return true;
     }
     return true;
 }
 
void merge(vector<string> &left, vector<string> &right, vector<string> &nums){
    int i = 0;
    int j = 0;
    while(i<left.size() && j<right.size()){
        if(check(left[i],right[j])){
            nums.push_back(left[i]);
            i++;
        }else{
            nums.push_back(right[j]);
            j++;
        }
    }
    while(i<left.size()){
        nums.push_back(left[i]);
        i++;
    }
    while(j<right.size()){
        nums.push_back(right[j]);
        j++;
    }
}

void merge_sort(vector<string> &nums){
    if(nums.size()<=1) return;
    vector<string> left, right;
    for(int i=0; i<nums.size()/2; i++){
        left.push_back(nums[i]);
    }
    for(int i=nums.size()/2; i<nums.size(); i++){
        right.push_back(nums[i]);
    }
    merge_sort(left);
    merge_sort(right);
    nums.clear();
    merge(left, right, nums);
}
 
vector<string> bigSorting(vector<string> unsorted) {
    merge_sort(unsorted);
    return unsorted;
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string n_temp;
    getline(cin, n_temp);

    int n = stoi(ltrim(rtrim(n_temp)));

    vector<string> unsorted(n);

    for (int i = 0; i < n; i++) {
        string unsorted_item;
        getline(cin, unsorted_item);

        unsorted[i] = unsorted_item;
    }

    vector<string> result = bigSorting(unsorted);

    for (size_t i = 0; i < result.size(); i++) {
        fout << result[i];

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

