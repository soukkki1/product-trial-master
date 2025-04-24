import { Component, OnInit, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { CartService } from 'app/cart/data-access/cart.service';
import { Product } from 'app/products/data-access/product.model';
import { CommonModule } from '@angular/common';      

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  standalone: true,
  imports: [CommonModule],
  styleUrls: ['./cart.component.scss'],
})
export class CartComponent implements OnInit {
  cartService = inject(CartService);
  items$: Observable<Product[]> = this.cartService.items$;

  ngOnInit(): void {}

  onRemove(product: Product) {
    this.cartService.removeFromCart(product);
  }

  trackById(_index: number, item: Product): number {
    return item.id;
  }
}