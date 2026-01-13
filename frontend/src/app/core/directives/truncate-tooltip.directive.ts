import { Directive, ElementRef, Input, Renderer2, AfterViewInit, OnDestroy } from '@angular/core';

@Directive({
  selector: '[appTruncateTooltip]',
  standalone: true
})
export class TruncateTooltipDirective implements AfterViewInit, OnDestroy {

  @Input('appTruncateTooltip') text: string = '';

  private observer!: MutationObserver;

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  ngAfterViewInit() {
    this.applyTooltip(); // initial check

    // Observe any DOM size/content changes
    this.observer = new MutationObserver(() => {
      this.applyTooltip();
    });

    this.observer.observe(this.el.nativeElement, {
      childList: true,
      subtree: true,
      characterData: true
    });

    // Also check after rendering cycles (for tabs/dialogs)
    setTimeout(() => this.applyTooltip(), 300);
  }

  private applyTooltip() {
    const element = this.el.nativeElement;

    const isTruncated = element.scrollWidth > element.clientWidth;

    if (isTruncated) {
      this.renderer.setAttribute(element, 'pTooltip', this.text);
      this.renderer.setAttribute(element, 'tooltipPosition', 'top');
      this.renderer.addClass(element, 'truncate');
    } else {
      this.renderer.removeAttribute(element, 'pTooltip');
    }
  }

  ngOnDestroy() {
    if (this.observer) {
      this.observer.disconnect();
    }
  }
}
