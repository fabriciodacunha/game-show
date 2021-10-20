import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RespostaDetailComponent } from './resposta-detail.component';

describe('Resposta Management Detail Component', () => {
  let comp: RespostaDetailComponent;
  let fixture: ComponentFixture<RespostaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RespostaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ resposta: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RespostaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RespostaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load resposta on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.resposta).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
