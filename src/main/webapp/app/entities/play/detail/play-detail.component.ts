import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlay } from '../play.model';

@Component({
  selector: 'jhi-play-detail',
  templateUrl: './play-detail.component.html',
})
export class PlayDetailComponent implements OnInit {
  play: IPlay | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ play }) => {
      this.play = play;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
