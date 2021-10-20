import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ParticipanteDetailComponent } from './participante-detail.component';

describe('Participante Management Detail Component', () => {
  let comp: ParticipanteDetailComponent;
  let fixture: ComponentFixture<ParticipanteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ParticipanteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ participante: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ParticipanteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ParticipanteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load participante on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.participante).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
