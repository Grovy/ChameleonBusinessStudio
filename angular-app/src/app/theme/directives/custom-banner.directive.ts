import { Directive, ElementRef, HostListener, Input, Renderer2 } from '@angular/core';

@Directive({
  selector: '[appCustomBannerDirective]'
})
export class CustomBannerDirective {
  @Input() appCustomBannerDirective = '';
  @Input() myBannerColor= '';

  constructor(private element: ElementRef, private renderer: Renderer2) { }

  @HostListener('change') onChange() {
    this.myBannerColor = this.appCustomBannerDirective || 'red';
  }

  ngOnInit() {
    this.renderer.setStyle(this.element.nativeElement, 'backgroundColor', this.myBannerColor);
  }

  refreshColor() {
    this.renderer.setStyle(this.element.nativeElement, 'backgroundColor', this.myBannerColor);
  }

}