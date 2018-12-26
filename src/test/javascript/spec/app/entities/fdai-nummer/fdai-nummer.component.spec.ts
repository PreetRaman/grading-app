/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GradingAppTestModule } from '../../../test.module';
import { FdaiNummerComponent } from 'app/entities/fdai-nummer/fdai-nummer.component';
import { FdaiNummerService } from 'app/entities/fdai-nummer/fdai-nummer.service';
import { FdaiNummer } from 'app/shared/model/fdai-nummer.model';

describe('Component Tests', () => {
    describe('FdaiNummer Management Component', () => {
        let comp: FdaiNummerComponent;
        let fixture: ComponentFixture<FdaiNummerComponent>;
        let service: FdaiNummerService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [GradingAppTestModule],
                declarations: [FdaiNummerComponent],
                providers: []
            })
                .overrideTemplate(FdaiNummerComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FdaiNummerComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FdaiNummerService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new FdaiNummer(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.fdaiNummers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
