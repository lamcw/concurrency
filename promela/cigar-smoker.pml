#include "include/sem.h"

byte agent_sem = 1;
byte mutex = 1;

byte tobacco = 0;
byte paper = 0;
byte match = 0;

byte tobacco_sem = 0;
byte paper_sem = 0;
byte match_sem = 0;

bool is_tobacco = false;
bool is_paper = false;
bool is_match = false;

active proctype agent() {
	do
	:: wait(agent_sem);
	   if
	   :: signal(tobacco);
	      signal(paper)
	   :: signal(paper);
	      signal(match)
	   :: signal(tobacco);
	      signal(match)
	   fi
	od
}

active proctype pusher_a() {
	do
	:: wait(tobacco);
	   wait(mutex);
	   if
	   :: is_paper -> is_paper = false; signal(match_sem)
	   :: is_match -> is_match = false; signal(paper_sem)
	   :: else -> is_tobacco = true
	   fi
	   signal(mutex)
	od
}

active proctype pusher_b() {
	do
	:: wait(paper);
	   wait(mutex);
	   if
	   :: is_tobacco -> is_tobacco = false; signal(match_sem)
	   :: is_match -> is_match = false; signal(tobacco_sem)
	   :: else -> is_paper = true
	   fi
	   signal(mutex)
	od
}

active proctype pusher_c() {
	do
	:: wait(match);
	   wait(mutex);
	   if
	   :: is_tobacco -> is_tobacco = false; signal(paper_sem)
	   :: is_paper -> is_paper = false; signal(tobacco_sem)
	   :: else -> is_match = true
	   fi
	   signal(mutex)
	od
}

active proctype smoker_tobacco() {
	do
	:: wait(tobacco_sem);
	   printf("Making cigar\n");
	   signal(agent_sem);
	   printf("Smoking\n")
	od
}

active proctype smoker_paper() {
	do
	:: wait(paper_sem);
	   printf("Making cigar\n");
	   signal(agent_sem);
	   printf("Smoking\n")
	od
}

active proctype smoker_match() {
	do
	:: wait(match_sem);
	   printf("Making cigar\n");
	   signal(agent_sem);
	   printf("Smoking\n")
	od
}
