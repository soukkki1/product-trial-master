import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Product } from 'app/products/data-access/product.model';
import { map } from 'rxjs/operators';

@Injectable({ 
    providedIn: "root"
}) export class CartService {
  private itemsSubject = new BehaviorSubject<Product[]>([]);
  items$: Observable<Product[]> = this.itemsSubject.asObservable();

  readonly count$: Observable<number> = this.items$.pipe(
    map(items => items.length)
  );

  addToCart(product: Product): void {
    const current = this.itemsSubject.value;
    this.itemsSubject.next([...current, product]);
  }

  removeFromCart(product: Product): void {
    const filtered = this.itemsSubject.value.filter(p => p.id !== product.id);
    this.itemsSubject.next(filtered);
  }

}
