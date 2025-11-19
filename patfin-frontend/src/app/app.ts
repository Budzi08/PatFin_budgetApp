import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FontSizeService } from './core/font-size.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'patfin-frontend';

  constructor(public fontSizeService: FontSizeService) {}

  increaseFont() {
    this.fontSizeService.increase();
  }

  decreaseFont() {
    this.fontSizeService.decrease();
  }
}
