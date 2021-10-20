import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EdicaoDetailComponent } from './edicao-detail.component';

describe('Edicao Management Detail Component', () => {
  let comp: EdicaoDetailComponent;
  let fixture: ComponentFixture<EdicaoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EdicaoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ edicao: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EdicaoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EdicaoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load edicao on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.edicao).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
