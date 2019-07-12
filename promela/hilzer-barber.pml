/* README!!
 * Run with Liveness -> non-progress cycles
 */
#include "include/sem.h"

#define NPROCS 40

int n = 20;
int customers = 0;
byte mutex = 1;
byte sofa = 4;
byte customer1 = 0;
byte customer2 = 0;
byte barber = 0;
byte payment = 0;
byte receipt = 0;
byte queue1[20];
byte queue2[20];
byte queue1_head = 0;
byte queue1_tail = n - 1;
byte queue2_head = 0;
byte queue2_tail = n - 1;

active proctype barber_proc() {
	do
	:: wait(customer1);
	   wait(mutex);
	   byte sem_pos = queue1_head;
	   queue1_head = (queue1_head + 1) % n;
	   signal(queue1[sem_pos])
	   wait(queue1[sem_pos])
	   signal(mutex);
	   signal(queue1[sem_pos])

	   wait(customer2);
	   wait(mutex);
	   sem_pos = queue2_head;
	   queue2_head = (queue2_head + 1) % n;
	   signal(mutex);
	   signal(queue2[sem_pos]);

	   signal(barber);
	   printf("Cut hair\n");

	   wait(payment);
	   printf("Accept payment\n");
	   signal(receipt)
	od
}

active [NPROCS] proctype customer_proc() {
	wait(mutex);
	if
	:: customers == n -> signal(mutex); goto end
	:: else
	fi
	customers++;
	queue1_tail = (queue1_tail + 1) % n;
	byte sem_pos = queue1_tail;
	queue1[sem_pos] = 0;
	signal(mutex);

	printf("Enter shop\n");
	signal(customer1);
	wait(queue1[sem_pos]);

	wait(sofa);
	printf("Sit on sofa\n");
	signal(queue1[sem_pos]);
	wait(mutex);
	queue2_tail = (queue2_tail + 1) % n;
	sem_pos = queue2_tail;
	queue2[sem_pos] = 0;
	signal(mutex);
	signal(customer2);
	wait(queue2[sem_pos]);
	signal(sofa);

	printf("Sit in barber chair\n");

	printf("Pay\n");
	signal(payment);
	wait(receipt);

	wait(mutex);
	customers--;
	signal(mutex)
end:
}
