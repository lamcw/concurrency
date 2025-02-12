#include "include/sem.h"
#define NOSTARVE
#define PID
#include "include/critical.h"

#define NPROCS 10

int n = 4;
int customers = 0;
byte mutex = 1;
byte customer = 0;
byte barber = 0;
byte customer_done = 0;
byte barber_done = 0;

active proctype barber_proc() {
end:	do
	:: wait(customer);
	   signal(barber);

	   printf("Cut hair\n");

	   wait(customer_done);
	   signal(barber_done)
	od
}

active [NPROCS] proctype customer_proc() {
	wait(mutex);
	critical_section();
	if
	:: customers == n -> signal(mutex);
	:: else ->
		customers++;
		signal(mutex);

		signal(customer);
		wait(barber);

		printf("Getting hair cut\n");

		signal(customer_done);
		wait(barber_done);

		wait(mutex);
		customers--;
		signal(mutex);
	fi
}
