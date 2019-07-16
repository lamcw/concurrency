#include "include/sem.h"

#define NPROCS 20

int n = 4;
int customers = 0;
byte mutex = 1;
byte customer = 0;
byte customer_done = 0;
byte barber_done = 0;
byte queue[4];
byte queue_head = 0;
byte queue_tail = n - 1;

active proctype barber_proc() {
end:	do
	:: wait(customer);
	   wait(mutex);
	   byte sem_pos = queue_head;
	   // take sem from queue
	   queue_head = (queue_head + 1) % n;
	   signal(mutex);

	   signal(queue[sem_pos]);

	   printf("Cut hair\n");

	   wait(customer_done);
	   signal(barber_done)
	od
}

active [NPROCS] proctype customer_proc() {
	wait(mutex);
	if
	:: customers == n -> signal(mutex);
	:: else ->
		customers++;
		queue_tail = (queue_tail + 1) % n;
		byte sem_pos = queue_tail;
		queue[sem_pos] = 0;
		signal(mutex);

		signal(customer);
		wait(queue[sem_pos]);

		printf("Getting hair cut\n");

		signal(customer_done);
		wait(barber_done);

		wait(mutex);
		customers--;
		signal(mutex);
	fi
}
