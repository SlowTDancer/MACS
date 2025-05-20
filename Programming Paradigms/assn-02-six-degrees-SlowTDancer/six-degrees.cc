#include <vector>
#include <list>
#include <queue>
#include <set>
#include <string>
#include <iostream>
#include <iomanip>
#include "imdb.h"
#include "path.h"
using namespace std;

/**
 * Using the specified prompt, requests that the user supply
 * the name of an actor or actress.  The code returns
 * once the user has supplied a name for which some record within
 * the referenced imdb existsif (or if the user just hits return,
 * which is a signal that the empty string should just be returned.)
 *
 * @param prompt the text that should be used for the meaningful
 *               part of the user prompt.
 * @param db a reference to the imdb which can be used to confirm
 *           that a user's response is a legitimate one.
 * @return the name of the user-supplied actor or actress, or the
 *         empty string.
 */

static string promptForActor(const string& prompt, const imdb& db)
{
  string response;
  while (true) {
    cout << prompt << " [or <enter> to quit]: ";
    getline(cin, response);
    if (response == "") return "";
    vector<film> credits;
    if (db.getCredits(response, credits)) return response;
    cout << "We couldn't find \"" << response << "\" in the movie database. "
	 << "Please try again." << endl;
  }
}

/**
 * Serves as the main entry point for the six-degrees executable.
 * There are no parameters to speak of.
 *
 * @param argc the number of tokens passed to the command line to
 *             invoke this executable.  It's completely ignored
 *             here, because we don't expect any arguments.
 * @param argv the C strings making up the full command line.
 *             We expect argv[0] to be logically equivalent to
 *             "six-degrees" (or whatever absolute path was used to
 *             invoke the program), but otherwise these are ignored
 *             as well.
 * @return 0 if the program ends normally, and undefined otherwise.
 */

bool check_cast(queue<path> &q, path now, vector<string> &cast, film movie, set<string> &used_actors, string target){
  for(int j = 0; j < cast.size(); j++){
        if(used_actors.count(cast[j])) continue;
        used_actors.insert(cast[j]);
        path new_path = now;
        new_path.addConnection(movie, cast[j]);
        if(cast[j] == target){
          cout << new_path << endl;
          return true;
        }
        q.push(new_path);
    }
    return false;
}

bool check_movies(queue<path> &q, path now, vector<film> &movies, imdb* db, set<string> &used_actors, set<film> &used_movies, string target){
  for(int i = 0; i < movies.size(); i++){
      if(used_movies.count(movies[i])) continue;
      used_movies.insert(movies[i]);
      film movie_now = movies[i];
      vector<string> cast;
      db->getCast(movie_now, cast);
      if(check_cast(q, now, cast, movie_now, used_actors, target)) return true;
  }
  return false;
}

void bfs(string source, string target, imdb* db){
  set<string> used_actors;
  set<film> used_movies;
  queue<path> q;
  path start(source);
  q.push(start);
  while(!q.empty() && q.front().getLength() < 6){
    path now = q.front();
    q.pop();
    string actor = now.getLastPlayer();
    vector<film> movies;
    db->getCredits(actor, movies);
    if(check_movies(q, now, movies, db, used_actors, used_movies, target)){
      return;
    }
  }
  cout << endl << "No path between those two people could be found." << endl << endl;
}




int main(int argc, const char *argv[])
{
  imdb db(determinePathToData(argv[1])); // inlined in imdb-utils.h
  if (!db.good()) {
    cout << "Failed to properly initialize the imdb database." << endl;
    cout << "Please check to make sure the source files exist and that you have permission to read them." << endl;
    return 1;
  }
  
  while (true) {
    string source = promptForActor("Actor or actress", db);
    if (source == "") break;
    string target = promptForActor("Another actor or actress", db);
    if (target == "") break;
    if (source == target) {
      cout << "Good one.  This is only interesting if you specify two different people." << endl;
    } else {
      bfs(source, target, &db); 
    }
  }

  cout << "Thanks for playing!" << endl;
  return 0;
}