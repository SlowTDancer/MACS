using namespace std;
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <unistd.h>
#include "imdb.h"
#include <iostream>
#include <cstring>

const char *const imdb::kActorFileName = "actordata";
const char *const imdb::kMovieFileName = "moviedata";

imdb::imdb(const string& directory)
{
  const string actorFileName = directory + "/" + kActorFileName;
  const string movieFileName = directory + "/" + kMovieFileName;
  
  actorFile = acquireFileMap(actorFileName, actorInfo);
  movieFile = acquireFileMap(movieFileName, movieInfo);
}

bool imdb::good() const
{
  return !( (actorInfo.fd == -1) || 
	    (movieInfo.fd == -1) ); 
}

// you should be implementing these two methods right here... 

int imdb::actorcmp(const void* a, const void* b){
  database* key_ptr = (database*)a;
  char* ptr1 = (char*)key_ptr->key;
  char* ptr2 = (char*)key_ptr->data + *(int*)b;
  return strcmp(ptr1, ptr2);
}

int imdb::moviecmp(const void* a, const void* b){
  database* key_ptr = (database*)a;
  film now; 
  film key = *(film*)(key_ptr->key); 
  string movie_now = "";
  char* ptr = (char*)(key_ptr->data) + *((int*)b);
  for(; *ptr != '\0'; ptr++){
    movie_now = movie_now + *ptr;
  }
  ptr++;
  now.title = movie_now;
  now.year = *(unsigned char*)(ptr) + 1900;
  if(key < now) return -1;
  if(key == now) return 0;
  return 1;
}

bool imdb::getCredits(const string& player, vector<film>& films) const {
  int* starting_pos = (int*)actorFile + 1;
  int elements = *(int*)actorFile;
  database info;
  info.key = (void*)player.c_str();
  info.data = actorFile;
  int* ptr = (int*)bsearch(&info, starting_pos, elements, sizeof(int), actorcmp);
  if(ptr == NULL) return false;
  int name_bytes = player.length() - player.length()%2 + 2;
  int* num_movies = (int*)((char*)actorFile + *ptr + name_bytes);
  short number_of_movies = *(short*)num_movies;
  int* movies = (int*)((char*)num_movies + sizeof(short) + (name_bytes + sizeof(short))%4);
  for(short i = 0; i < number_of_movies; i++){
    film movie;
    int movie_location = movies[i];
    string title = "";
    char* ch =(char*)movieFile + movie_location;
    for(; *ch != '\0' ; ch++){
      title += *ch;
    }
    ch++;
    movie.title = title;
    movie.year = *(ch) + 1900;
    films.push_back(movie);
  }
  return true;
}

bool imdb::getCast(const film& movie, vector<string>& players) const {
  int* start = (int*)movieFile +1;
  database key;
  key.data = movieFile;
  key.key = (void*)&movie;
  int* ptr = (int*)bsearch(&key, start, *(int*)movieFile, sizeof(int), moviecmp);
  if(ptr == NULL) return false;
  int distance = movie.title.length() + 1 + 1 + movie.title.length()%2;
  int* number_of_actors = (int*)((char*)movieFile + *ptr + distance);
  short num = *(short*)((char*)number_of_actors);
  int* actors = (int*)((char*)number_of_actors + sizeof(short) + (distance + sizeof(short))%4);
  for(short i = 0; i < num; i++){
    int actor_location = actors[i];
    string player = "";
    char* ch =(char*)actorFile + actor_location;
    for(; *ch != '\0' ; ch++){
      player += *ch;
    }
    players.push_back(player);
  }
  return false;
}

imdb::~imdb()
{
  releaseFileMap(actorInfo);
  releaseFileMap(movieInfo);
}

// ignore everything below... it's all UNIXy stuff in place to make a file look like
// an array of bytes in RAM.. 
const void *imdb::acquireFileMap(const string& fileName, struct fileInfo& info)
{
  struct stat stats;
  stat(fileName.c_str(), &stats);
  info.fileSize = stats.st_size;
  info.fd = open(fileName.c_str(), O_RDONLY);
  return info.fileMap = mmap(0, info.fileSize, PROT_READ, MAP_SHARED, info.fd, 0);
}

void imdb::releaseFileMap(struct fileInfo& info)
{
  if (info.fileMap != NULL) munmap((char *) info.fileMap, info.fileSize);
  if (info.fd != -1) close(info.fd);
}
