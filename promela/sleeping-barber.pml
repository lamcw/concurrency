#include "include/sem.h"

int n = 4
int customers = 0
byte mutex = 1
byte customer = 0
byte barber = 0
byte customer_done = 0
byte barber_done = 0

active proctype barber_proc() {
	do
	:: wait(customer);
	   signal(barber);

	   printf("Cut hair\n");

	   wait(customer_done);
	   signal(barber_done)
	od
}

proctype customer_proc() {
	wait(mutex);
	if
	:: customers == n -> signal(mutex); goto L1
	fi
	signal(mutex);

	signal(customer);
	wait(barber);

	printf("Getting hair cut\n");

	signal(customer_done);
	wait(barber_done);

	wait(mutex);
	customer--;
	signal(mutex);

L1:
	skip
}

init {
	do
	:: run customer_proc()
	od
}
