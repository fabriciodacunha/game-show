import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlayDetailComponent } from './play-detail.component';

describe('Play Management Detail Component', () => {
  let comp: PlayDetailComponent;
  let fixture: ComponentFixture<PlayDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlayDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ play: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PlayDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PlayDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load play on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.play).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
