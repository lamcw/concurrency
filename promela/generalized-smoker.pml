#include "include/sem.h"

byte mutex = 1;

byte tobacco = 0;
byte paper = 0;
byte match = 0;

byte tobacco_sem = 0;
byte paper_sem = 0;
byte match_sem = 0;

int n_tobacco = 0;
int n_paper = 0;
int n_match = 0;

active proctype agent() {
	do
	:: signal(tobacco); signal(paper)
	:: signal(paper); signal(match)
	:: signal(tobacco); signal(match)
	od
}

active proctype pusher_a() {
	do
	:: wait(tobacco);
	   wait(mutex);
	   if
	   :: (n_paper > 0) -> n_paper--; signal(match_sem)
	   :: (n_match > 0) -> n_match--; signal(paper_sem)
	   :: else -> n_tobacco++
	   fi
	   signal(mutex)
	od
}

active proctype pusher_b() {
	do
	:: wait(paper);
	   wait(mutex);
	   if
	   :: (n_tobacco > 0) -> n_tobacco--; signal(match_sem)
	   :: (n_match > 0) -> n_match--; signal(tobacco_sem)
	   :: else -> n_paper++
	   fi
	   signal(mutex)
	od
}

active proctype pusher_c() {
	do
	:: wait(match);
	   wait(mutex);
	   if
	   :: (n_tobacco > 0) -> n_tobacco--; signal(paper_sem)
	   :: (n_paper > 0) -> n_paper--; signal(tobacco_sem)
	   :: else -> n_match++
	   fi
	   signal(mutex)
	od
}

active proctype smoker_tobacco() {
	do
	:: wait(tobacco_sem);
	   printf("Making cigar\n");
	   printf("Smoking\n")
	od
}

active proctype smoker_paper() {
	do
	:: wait(paper_sem);
	   printf("Making cigar\n");
	   printf("Smoking\n")
	od
}

active proctype smoker_match() {
	do
	:: wait(match_sem);
	   printf("Making cigar\n");
	   printf("Smoking\n")
	od
}
