import { CommonModule } from '@angular/common';
import { CssSelector } from '@angular/compiler';
import { Component, ElementRef, Input, AfterViewInit, ViewChild } from '@angular/core';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'app-truncate-text',
  standalone: true,
  imports: [TooltipModule, CommonModule],
  templateUrl: './truncate-text.component.html',
})
export class TruncateTextComponent implements AfterViewInit {
  @Input() text: string = '';
  @Input() class: string = ''
  @ViewChild('textRef') textRef!: ElementRef;

  isTruncated = false;

  ngAfterViewInit() {
    setTimeout(() => {
      const el = this.textRef.nativeElement;
      this.isTruncated = el.scrollWidth > el.clientWidth;
    });
  }
}
