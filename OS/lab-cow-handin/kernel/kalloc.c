// Physical memory allocator, for user processes,
// kernel stacks, page-table pages,
// and pipe buffers. Allocates whole 4096-byte pages.

#include "types.h"
#include "param.h"
#include "memlayout.h"
#include "spinlock.h"
#include "riscv.h"
#include "defs.h"


#define LAW(pa) (uint64)((uint64)pa - KERNBASE)/4096

void freerange(void *pa_start, void *pa_end);

extern char end[]; // first address after kernel.
                   // defined by kernel.ld.

struct run {
  struct run *next;
};

struct {
  struct spinlock lock;
  struct run *freelist;
} kmem;

struct {
  struct spinlock reference_lock;
  int references[(PHYSTOP - KERNBASE) / PGSIZE];
}rc;

void increase(uint64 pa){
  acquire(&rc.reference_lock);
  rc.references[LAW(pa)]++;
  release(&rc.reference_lock);
}

void decrease(uint64 pa){
  acquire(&rc.reference_lock);
  rc.references[LAW(pa)]--;
  release(&rc.reference_lock);
}


void
kinit()
{ 
  initlock(&kmem.lock, "kmem");
  initlock(&rc.reference_lock, "reference_lock");
  freerange(end, (void*)PHYSTOP);
}

void
freerange(void *pa_start, void *pa_end)
{
  char *p;
  p = (char*)PGROUNDUP((uint64)pa_start);
  acquire(&rc.reference_lock);
  for(int i = 0; i < (PHYSTOP - KERNBASE) / PGSIZE; i++){
    rc.references[i] = 1;
  }
  release(&rc.reference_lock);
  for(; p + PGSIZE <= (char*)pa_end; p += PGSIZE)
    kfree(p);
}

// Free the page of physical memory pointed at by pa,
// which normally should have been returned by a
// call to kalloc().  (The exception is when
// initializing the allocator; see kinit above.)
void
kfree(void *pa)
{
  struct run *r;

  if(((uint64)pa % PGSIZE) != 0 || (char*)pa < end || (uint64)pa >= PHYSTOP)
    panic("kfree");

  decrease((uint64)pa);

  acquire(&rc.reference_lock);
  if(rc.references[LAW(pa)] > 0){
    release(&rc.reference_lock);
    return;
  }
  release(&rc.reference_lock);

  // Fill with junk to catch dangling refs.
  memset(pa, 1, PGSIZE);

  r = (struct run*)pa;

  acquire(&kmem.lock);
  r->next = kmem.freelist;
  kmem.freelist = r;
  release(&kmem.lock);
}

// Allocate one 4096-byte page of physical memory.
// Returns a pointer that the kernel can use.
// Returns 0 if the memory cannot be allocated.
void *
kalloc(void)
{
  struct run *r;

  acquire(&kmem.lock);
  r = kmem.freelist;
  if(r)
    kmem.freelist = r->next;
  release(&kmem.lock);
  
  if(r){
    memset((char*)r, 5, PGSIZE); // fill with junk
    acquire(&rc.reference_lock);
    rc.references[LAW(r)] = 1;
    release(&rc.reference_lock);
  }
  return (void*)r;
}
